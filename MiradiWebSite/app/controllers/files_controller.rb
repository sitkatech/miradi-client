class FilesController < ApplicationController
  before_filter :login_required

  def index
    redirect_to :action => 'files'
  end
  
  def files
	list_files(get_private_directory)
  end
  
  def list_files(dir)
    @sizeK = Hash.new
	Dir.foreach(dir) do | f |
	  @sizeK[f] = File.size(File.join(dir, f)) / 1024
	end
  end
  
  def file
	download_file(get_private_directory)
  end
  
  def download_file(dir)
	file = params[:file]

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
	logger.info("DOWNLOAD: #{file} by #{session[:user].email} at #{Time.now}")
	send_file(path, {:stream => false, :type => type, :disposition => disposition})
  end
  
  def untested
    list_files(get_untested_directory)
  end
  
  def file_untested
	download_file(get_untested_directory)
  end
  
  def get_private_directory
	return File.join(RAILS_ROOT,'private')
  end

  def get_untested_directory
	return File.join(File.join(RAILS_ROOT,'private'), 'untested')
  end
end
