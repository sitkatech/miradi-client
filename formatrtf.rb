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

class RtfFormatter
	def initialize(file)
		@indent = 0
		@commands_on_line = 0
		@contents = IO.read(file).gsub("\n", ' ')
	end
	
	def format_rtf()
		while(!@contents.empty?) do 
		
			command = @contents.match(/^\\[\w\d\*-]*/)
			if(command)
				cmd = command[0]
				
				if(@commands_on_line == 0)
					newline
				end
				if(@commands_on_line > 8)
					newline
				end
				print "#{cmd} "
				@commands_on_line += 1
				if(forces_newline?(cmd))
					newline
				end
				@contents = @contents.sub(cmd, '')
				next
			end
			
			whitespace = @contents.match(/^\s\s*/)
			if(whitespace)
				#print whitespace[0]
				@contents = @contents.sub(whitespace[0], '')
				next
			end
			

			first = @contents[0,1]
			if(first == '{')
				newline
				print first
				@indent += 1
				@contents = @contents[1...-1]
				next
			end
			
			if(first == '}')
				@indent -= 1
				newline
				print first
				@contents = @contents[1...-1]
				next
			end
			
			other = @contents.match(/^(.*?)([\\\{\}])/)
			if(other)
				if(@commands_on_line != 0)
					newline
				end
				print other[1]
				@contents = @contents.sub(other[1], '')
				next
			end
			
			puts "FAILED: #{@contents[0,100]}"
			exit(1)
		end
		
		puts
	end
	
	def newline
		puts
		@indent.times do
			print ' '
		end
		@commands_on_line = 0
	end
	
	def forces_newline?(cmd)
		if(cmd =~ /\\cell[\d*]/) then return true end
		if(cmd =~ /\\cellx[\d*]/) then return true end
		if(cmd =~ /\\row/) then return true end
		return false
	end
	
end


file = ARGV[0]
if(!File.exist?(file))
	puts "File not found: #{file}"
	exit(1)
end

RtfFormatter.new(file).format_rtf
