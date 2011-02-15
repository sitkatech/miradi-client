# merge FieldLabels.properties into miradi.pot

properties_file = ARGV[0]
pot_file = ARGV[1]

File.open(pot_file, 'a') do | output |
	File.foreach(properties_file) do | line |
		if line.index('#') == 0
			next
		end
		key, value = line.split('=', 2)
		if !key || !value
			next
		end
		value.strip!
		if value.length == 0
			next
		end
		type, tag = key.split('.')
		output.puts
		output.puts "msgid \"FieldLabel|#{key.strip}|#{value.strip}\""
		output.puts "msgstr \"\""
	end	
end
