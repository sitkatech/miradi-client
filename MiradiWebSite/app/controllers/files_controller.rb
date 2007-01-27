class FilesController < ApplicationController
  before_filter :login_required

  def index
    redirect_to :action => 'files'
  end
  
  def files
    @sizeK = Hash.new
	dir = get_private_directory
	Dir.foreach(dir) do | f |
	  @sizeK[f] = File.size(File.join(dir, f)) / 1024
	end
  end
  
  def file
	file = params[:file]

	dir = get_private_directory
	
	if(file.index('.') == 0 || !Dir.entries(dir).index(file))
      flash[:warning]  = "File not found"
	  redirect_to "/download"
	  return
	end
	type = 'application/octet-stream'
	disposition = 'attachment'
	if(file == 'README')
	  type = 'text'
	  disposition = 'inline'
	elsif(file.index('.pdf'))
	  type = 'application/pdf'
	end
	path = File.join(dir, file)
	raise Exception, "Cannot read file #{path}" unless File.exist?(path)# and File.readable?(path)
	send_file(path, {:stream => false, :type => type, :disposition => disposition})
  end
  
  def get_private_directory
	return File.join(RAILS_ROOT,'private')
  end
end
