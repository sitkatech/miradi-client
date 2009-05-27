class MiradiServerController < Controller
	map '/MiradiServer'
	engine :None

	FILE_NOT_FOUND = 404
	DATA_DIRECTORY = '/var/local/MiradiServer/projects'
	LOCK_NAME = '/lock'
		
	# the index action is called automatically when no other action is specified
	def index
	    @title = "Miradi Data Server"
	    "<html>Welcome to the Miradi Data Server!<br/>" +
		"<form action='projects' method='post' accept-charset='utf8'>" + 
		"Project Name: <input type='text' name='name'></input>" +
		"<input type='submit' value='Create Project'></input>" + 
		"</form>"
	end

	def projects *args
		if args.size == 0
			return manage_projects
		end
		
		project = args.shift
		relative_path = args.join('/')
		
		if request.get? && request.params.has_key?("Manifests")
			attempt_get_manifests project
		elsif request.get? && request.params.has_key?("files")
			attempt_read_multiple project, request.params["files"]
		elsif request.get?
			attempt_read project, relative_path
		elsif request.post? && request.params.has_key?("WriteMultiple")
			attempt_write_multiple project
		elsif request.post?
			attempt_write project, relative_path, request[:data]
		elsif request.delete? && request.params.has_key?("DeleteMultiple")
			attempt_delete_multiple project
		elsif request.delete?
			attempt_delete project, relative_path
		end
	end
	
	def manage_projects
		if request.get?
			return get_project_list
		elsif request.post?
			return create_project
		end
	end
	
	def get_project_list
		project_list = []
		Dir.new(DATA_DIRECTORY).each do |f| 
			if f.index('.') == 0
				next
			end
			project_list << f  
		end
		return project_list.join("\n")
	end
	
	def create_project
		name = request[:CreateProject]
		absolute_path = File.join(DATA_DIRECTORY, name)
		if name =~ /^\w*$/
			Dir.mkdir(absolute_path)
			puts "Created project: #{absolute_path}" 
			return "OK"
		else
			"Illegal characters in #{name}. Go back and try again"
		end
	end
	
	def does_file_exist(absolute_path)
		return File.exists?(absolute_path) ? "Exists" : "No such file: #{absolute_path}"
	end
	
	def attempt_get_manifests(project)
		puts "attempt_get_manifests"
		result = ""
		
		absolute_path = create_absolute_path(project, "json")
		prefix = "objects-"
		prefix_length = prefix.size
		Dir.foreach(absolute_path) do |entry|
			if(entry.index(prefix) != 0)
				next
			end
			path = File.join(absolute_path, entry)
			if(!File.directory?(path))
				next
			end
			type = entry[prefix_length..-1]
			manifest = File.join(path, "manifest")
			if(!File.exists?(manifest))
				next
			end
			contents = File.read(manifest)
			result << type + "\n"
			result << contents + "\n"
		end
		
		return result
	end
	
	def attempt_read_multiple(project, files)
		result = ""
		files = files.split(',')
		files.each do |path|
			result << path + "\n"
			result << attempt_read(project, path) + "\n"
		end
		
		return result
	end
		
	def attempt_read(project, relative_path)
		absolute_path = create_absolute_path(project, relative_path)
		if request.params.has_key?("Exists")
			return does_file_exist(absolute_path)
		end

		project_path = create_project_path(project)
		if !File.exists?(project_path) || !File.directory?(project_path)
			response.status = FILE_NOT_FOUND
			return ""
		end

		if !File.exists?(absolute_path)
			response.status = FILE_NOT_FOUND
			return ""
		end
		result = File.read(absolute_path)
		if(result[-1,1] != "\n")
			result << "\n"
		end
		return result
	end
	
	def attempt_write_multiple(project)
		prefix = "File."
		prefix_length = prefix.length
		request.params.each_key do |key|
			if key.index(prefix) != 0
				next
			end
			file = key[prefix_length..-1]
			attempt_write(project, file, request.params[key])
		end
	end

	def attempt_write(project, relative_path, data)
		project_path = create_project_path(project)
		if !File.exists?(project_path) || !File.directory?(project_path)
			response.status = FILE_NOT_FOUND
			return ""
		end

		absolute_path = create_absolute_path(project, relative_path)
		if request.params.has_key?("Lock")
			puts "Checking lock #{absolute_path}"
			if(File.exists?(absolute_path))
				response.status = 415
				return "Project is already locked"
			end
		end

		mkdirs(project, File.dirname(relative_path))
		File.open(absolute_path, "w") do |f|
			f.write data
		end
		"OK"
	end
	
	def mkdirs(project, relative_path)
		path_to_create = create_absolute_path(project, relative_path)
		if File.exists? path_to_create
			return
		end
		parent = File.dirname(relative_path)
		if parent.length > 0 && parent != '.'
			mkdirs(project, parent)
		end
		Dir.mkdir(path_to_create)
	end
	
	def create_absolute_path(project, relative_path)
		return File.join(create_project_path(project), relative_path)
	end
	
	def create_project_path(project)
		return File.join(DATA_DIRECTORY, project)
	end
	
	def attempt_delete_multiple(project)
		prefix = "File."
		prefix_length = prefix.length
		request.params.each_key do |key|
			if key.index(prefix) != 0
				next
			end
			file = key[prefix_length..-1]
			attempt_delete(project, file)
		end
	end

	def attempt_delete(project, relative_path)
		project_path = create_project_path(project)
		if !File.exists?(project_path) || !File.directory?(project_path)
			response.status = FILE_NOT_FOUND
			return ""
		end

		absolute_path = create_absolute_path(project, relative_path)
		if !File.exists? absolute_path 
			puts "Attempt to delete non-existant file/directory: #{absolute_path}"
			response.status = FILE_NOT_FOUND
			return ""
		end
		if File.dirname(absolute_path) == DATA_DIRECTORY
			attempt_delete_project project
			return
		end
		
		if File.directory? absolute_path
			throw IOError.new("Cannot delete directory: #{absolute_path}")
		end
		File.delete(absolute_path)
		puts "Deleted file #{absolute_path}"
		"OK"
	end	
	
	def attempt_delete_project(project)
		if !request.params.has_key?("DeleteProject")
			throw IOError.new("Cannot delete project without DeleteProject parameter")
		end
		path_to_delete = create_project_path(project)
		puts "Deleting project #{path_to_delete}"
		rmdirs(path_to_delete)
		"OK"
	end
	
	def rmdirs(absolute_path)
		filename = File.basename(absolute_path)
		if (filename == '.') || (filename == '..')
			return
		end
		if !File.exists?(absolute_path)
			return
		end 
		if File.directory?(absolute_path)
			Dir.foreach(absolute_path) do | entry |
				rmdirs(File.join(absolute_path, entry))
			end
			Dir.rmdir(absolute_path)
		else
			File.delete absolute_path
		end
	end
end
