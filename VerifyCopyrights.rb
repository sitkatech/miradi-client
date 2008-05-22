#!/bin/env ruby

$COPYRIGHT = [
'Copyright 2005-2008, Foundations of Success, Bethesda, Maryland',
'(on behalf of the Conservation Measures Partnership, "CMP") and',
'Beneficent Technology, Inc. ("Benetech"), Palo Alto, California.',
'',
'This file is part of Miradi',
'',
'Miradi is free software: you can redistribute it and/or modify',
'it under the terms of the GNU General Public License version 3,',
'as published by the Free Software Foundation.',
'',
'Miradi is distributed in the hope that it will be useful,',
'but WITHOUT ANY WARRANTY; without even the implied warranty of',
'MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the',
'GNU General Public License for more details.',
'',
'You should have received a copy of the GNU General Public License',
'along with Miradi.  If not, see <http://www.gnu.org/licenses/>.',
]

def verify_tree(directory)
	Dir.foreach(directory) do | child |
		if(child =~ /^\./)
			next
		end
		
		file = File.join(directory, child)
		if(File.directory?(file))
			verify_tree(file)
		else
			verify_file(file)
		end
	end
end

def verify_file(file)
	if file =~ /\.java/
		verify_file_copyright(file)
	elsif file =~ /\.html/
		verify_file_copyright(file)
	end
end

def verify_file_copyright(file)
	contents = IO.readlines(file)
	first = find_line_with_copyright(contents, 0)
	if(!first)
		puts "Missing copyright: #{file}"
		return
	end
	if find_line_with_copyright(contents, first+1)
		puts "Second copyright: #{file}"
	end
	$COPYRIGHT.size.times do | line |
		if contents.size <= first+line
			puts "Copyright doesn't have enough lines: #{file}"
			return
		end
		if !contents[first + line].index($COPYRIGHT[line])
			puts "Incomplete copyright: #{file}"
			return
		end
	end
end

def find_line_with_copyright(lines, start_at)
	(start_at..lines.size).each do | index |
		if lines[index] =~ /Copyright/
			return index
		end
	end
	return nil
end


def verify_html_file(file)
	puts("HTML: #{file}")

end

if ARGV.length != 1
	puts "Usage: VerifyCopyrights <directory>"
	exit 1
end

verify_tree(ARGV[0])

