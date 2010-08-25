# merge HTML files into miradi.pot, one entry per HTML file

html_root_directory = ARGV[0]
pot_file = ARGV[1]

if(!File.directory?(html_root_directory))
	puts "#{html_root_directory} not a valid directory"
	exit(1)
end

def append_entry_to_pot(output, text)
	text.gsub!(/\\/, '\\\\\\\\') # Not sure why I need so many backslashes
	text.gsub!(/\"/, '\\"')

	output.puts
	output.puts "msgid \"#{text}\""
	output.puts "msgstr \"\""
end

def process_html_file(output, root_directory, relative_file)
	file = File.join(root_directory, relative_file)
	lines = File.readlines(file)
	lines = lines.collect do | line |
		line.chomp
	end
	full_text = lines.join.gsub(/<!--.*?-->/, '')
	append_entry_to_pot(output, "html|#{relative_file}|#{full_text}")
end

def process_directory(output, root_directory, relative_directory)
	directory = File.join(root_directory, relative_directory)
	Dir.entries(directory).each do | entry |
		if(entry.index('.') == 0)
			next
		end
		file = File.join(directory, entry)
		new_relative_directory = File.join(relative_directory, entry)
		if(File.directory?(file))
			process_directory(output, root_directory, new_relative_directory)
		elsif file.index('.html')
			process_html_file(output, root_directory, new_relative_directory)
		end
	end
end

File.open(pot_file, 'a') do | output |
	process_directory(output, File.expand_path(html_root_directory), '')
end

