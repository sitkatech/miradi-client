# merge choice tsv files into miradi.pot, one pot entry per choice entry

TAB_SUBSTITUTE = "___"

choice_root_directory = ARGV[0]
pot_file = ARGV[1]

if(!File.directory?(choice_root_directory))
	puts "#{choice_root_directory} not a valid directory"
	exit(1)
end

def append_entry_to_pot(output, text)
	text.gsub!(/\\/, '\\\\\\\\') # Not sure why I need so many backslashes
	text.gsub!(/\"/, '\\"')

	output.puts
	output.puts "msgid \"#{text}\""
	output.puts "msgstr \"\""
end

def process_tsv_file(output, root_directory, relative_file)
	file = File.join(root_directory, relative_file)
	lines = File.readlines(file)
	lines.each do | line |
		line.gsub!("#.*", "")
		line.strip!
		if(line.empty?)
			next
		end
		
		elements = line.split("\t")
		elements.shift	# discard code element
		line = elements.join(TAB_SUBSTITUTE)

		append_entry_to_pot(output, "choice|#{relative_file}|#{line}")
	end
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
		elsif file.index('.tsv')
			process_tsv_file(output, root_directory, new_relative_directory)
		end
	end
end

File.open(pot_file, 'a') do | output |
	process_directory(output, File.expand_path(choice_root_directory), '')
end

