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
			rtf = @contents.match(/^\{\\rtf1/)
			if(rtf)
				print(rtf)
				@indent = 1
				remove(rtf[0])
				next
			end
			
			command = @contents.match(/^\\[\w\d\*-]*/)
			if(command)
				cmd = command[0]
				
				if(forces_newline_before?(cmd))
					newline
				end
				print "#{cmd} "
				@commands_on_line += 1
				if(forces_newline_after?(cmd))
					newline
				end
				
				@previous_command = cmd
				remove(cmd)
				next
			end
			
			whitespace = @contents.match(/^\s\s*/)
			if(whitespace)
				#print whitespace[0]
				remove(whitespace[0])
				next
			end
			

			first = @contents[0,1]
			if(first == '{')
				newline
				print first
				@indent += 1
				remove(first)
				next
			end
			
			if(first == '}')
				@indent -= 1
				newline
				print first
				remove(first)
				next
			end
			
			other = @contents.match(/^(.*?)([\\\{\}])/)
			if(other)
				if(@commands_on_line != 0)
					newline
				end
				print other[1]
				remove(other[1])
				next
			end
			
			puts "FAILED: #{@contents[0,100]}"
			exit(1)
		end
		
		puts
	end
	
	def remove(leading_string)
		@contents = @contents[leading_string.length..-1]
	end
	
	def newline
		puts
		@indent.times do
			print '  '
		end
		@commands_on_line = 0
	end
	
	def forces_newline_before?(cmd)
		if(@commands_on_line == 0 || @commands_on_line > 8)
			return true
		end
		
		if(cmd == "\\trowd")
			return true
		end
		
		if(@previous_command =~ /\\tr/ && cmd =~ /\\tbl/)
			return true
		end
		
		if( (@previous_command =~ /\\tbl/ || @previous_command =~ /\\tr/) && 
				cmd =~ /\\cl/)
			return true
		end
		
		return false
	end
	
	def forces_newline_after?(cmd)
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
