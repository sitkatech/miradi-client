import sys
import argparse
import csv
import re
import string
# this is a hack to be able to use polib without installing it, it needs to be in a directory with this name relative to where this script is being called
sys.path.append("./polib-1.0.4")
import polib


class GlossaryEntry(object):
    """
    Takes a string array (row from a csv file) and assumes the first element is the msgid and the last is the msgstr
    """
    def __init__(self, str_arr):
        super(GlossaryEntry, self).__init__()
        if len(str_arr) < 2:
            raise ValueError("The glossary CSV file needs to have at least 2 columns.")
        self.term = unicode(str_arr[0], "utf-8")
        self.translation = unicode(str_arr[-1], "utf-8")

    def __str__(self):
        return u"GlossaryEntry(" + self.term + u", " + self.translation + u")"


class ReplacementRange(object):
    def __init__(self, start_index, str_len):
        super(ReplacementRange, self).__init__()
        self.start_index = start_index
        self.end = start_index + str_len

    def is_overlap(self, other_range):
        return other_range.start_index < self.end and other_range.end > self.start_index

    def sub_string(self, text_for_range):
        return text_for_range[self.start_index:self.end]


class TextReplacementManager(object):
    def __init__(self, original_text):
        super(TextReplacementManager, self).__init__()
        self.original_text = original_text
        self.replacement_text = unicode(original_text)
        self.replacement_ranges = []

    def replace_text(self, search_text, new_text):
        locations_found = [m.start() for m in re.finditer(search_text, self.original_text, re.IGNORECASE)]
        if len(locations_found) == 0:
            return
        search_len = len(search_text)
        for loc in locations_found:
            location_range = ReplacementRange(loc, search_len)
            if not any(rng.is_overlap(location_range) for rng in self.replacement_ranges):
                self.replacement_ranges.append(location_range)
                temp_new_text = unicode(new_text)
                og_text = location_range.sub_string(self.original_text)
                if self.are_all_capitalized(og_text):
                    temp_new_text = self.capitalize_all(temp_new_text)
                elif self.is_first_capitalized(og_text):
                    temp_new_text = self.capitalize_first(temp_new_text)
                # replace just the first match of search_text
                self.replacement_text = re.sub(og_text, temp_new_text, self.replacement_text, count=1)

    def is_first_capitalized(self, some_text):
        if some_text == u"":
            return False
        return some_text[0].isupper()

    def are_all_capitalized(self, some_text):
        words = some_text.split()
        return all(self.is_first_capitalized(w) for w in words)

    def capitalize_first(self, some_text):
        if some_text == u"":
            return u""
        return some_text[0].upper() + some_text[1:]

    def capitalize_all(self, some_text):
        return string.capwords(some_text)


def read_glossary_entries(file_name):
    """
    reads all of the GlossaryEntry rows from the file, and sorts them by term length descending
    """
    pairs = []
    with open(file_name, "rb") as csv_file:
        rdr = csv.reader(csv_file)
        # skip the header row
        next(rdr)
        for row in rdr:
            pairs.append(GlossaryEntry(row))
    pairs = sorted(pairs, key = lambda entry: len(entry.term))
    pairs.reverse()
    return pairs


def clean_prefixed_entries(msgid):
    prefixes = [u"html|", u"FieldLabel|", u"choice|"]
    if any(msgid.startswith(p) for p in prefixes):
        # return the text after the final pipe, which may be the empty string
        pipe_split = msgid.split(u"|")
        thing_to_translate = pipe_split[-1]
        prefix = msgid[:-len(thing_to_translate)]
        return prefix, thing_to_translate
    return u"", msgid


def replace_with_glossary(glossary, msgstr):
    """
    :type glossary: list[GlossaryEntry]
    :type msgstr: unicode
    :rtype: unicode
    """
    manager = TextReplacementManager(msgstr)
    for entry in glossary:
        manager.replace_text(entry.term, entry.translation)
    return manager.replacement_text


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("inputFile", help="The name of the PO file that will have messages replaced.")
    parser.add_argument("glossaryFile", help="The name of the CSV file that has the messages to replace.  The format is assumed to be the first column is the msgid and the last column is the new msgstr.")
    parser.add_argument("outputFile", help="The name of the file to create with the replaced messages.")
    args = parser.parse_args()

    input_file_name = args.inputFile
    glossary_file_name = args.glossaryFile
    output_file_name = args.outputFile

    glossary = read_glossary_entries(glossary_file_name)

    input_po = polib.pofile(input_file_name)
    for entry in input_po.translated_entries():
        prefix, thing_to_translate = clean_prefixed_entries(entry.msgid)
        translated_text = replace_with_glossary(glossary, thing_to_translate)
        entry.msgstr = prefix + translated_text

    input_po.save(output_file_name)



if __name__ == "__main__":
    main()