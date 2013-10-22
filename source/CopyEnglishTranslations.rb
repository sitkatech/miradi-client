#!/usr/bin/env ruby

pofile = ARGV[0]
puts "This will copy all the English strings into the target strings,"
puts "but stripping the prefix(es) before the | symbols."
puts "PO File: #{pofile}"

if !pofile
	puts "Must specify a PO file"
	exit 1
end

class Worker
	def initialize
		@out = STDOUT
	end
	
	def parse_file(file)
		@file = file
		File.open(@file) do | input |
			parse_stream(input)
		end
	end
	
	def parse_stream(input)
		@in = input
		while !@in.eof? do
			parse_line(@in.readline)
		end
	end
	
	def parse_line(line)
		stripped = line.strip
		if stripped.empty?
			@out.puts line
		elsif stripped.index("#") == 0
			@out.puts line
		elsif stripped.index("msgid") == 0
			process_msgid(line)
		elsif stripped.index("msgstr") == 0
			process_msgstr(line)
		else
			raise "Unknown line: #{line}"
		end
	end
	
	def process_msgid(line)
		@out.puts line
		remainder = line.sub("msgid", "").strip
		next_line = capture_remainder_and_following(remainder) do |line|
			@out.puts line
		end
		@msgid = @captured
		if next_line
			parse_line next_line
		end
	end
	
	def process_msgstr(line)
		remainder = line.sub("msgstr", "").strip
		next_line = capture_remainder_and_following(remainder) do
			# nothing required 
		end
		@msgstr = @captured
		if @msgstr.empty?
			@msgstr = @msgid
		end
		@msgstr = strip_contexts(@msgstr)
		@out.puts "msgstr \"#{@msgstr}\""
		if !@in.eof?
			parse_line next_line
		end
	end
	
	def capture_remainder_and_following(remainder)
		start_quote = remainder.index("\"")
		end_quote = remainder.rindex("\"")
		@captured = remainder.slice(start_quote+1, end_quote-1)
		while !@in.eof?
			line = @in.readline
			
			stripped = line.strip
			if stripped.empty? || stripped.index("msgid") == 0 || stripped.index("msgstr") == 0
				return line
			end
			yield line
			start_quote = line.index("\"")
			end_quote = line.rindex("\"")
			@captured += line.slice(start_quote+1, end_quote-1)
		end
		
		return nil
	end

	def strip_contexts(entry)
		while has_context?(entry)
			entry = strip_context(entry)
		end
		return entry
	end
		
	def strip_context(entry)
		extract_context = get_context(entry)
		if extract_context
			return extract_context[1]
		end
	end
	
	def has_context?(entry)
		return get_context(entry)
	end
	
	def get_context(entry)
		return entry.match /\w*\|(.*)/
	end
end

worker = Worker.new
worker.parse_file(pofile)
