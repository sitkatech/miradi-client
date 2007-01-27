class FilesController < ApplicationController
  before_filter :login_required

  def index
    render :action => 'files'
  end
  
  def file
	file = params[:file]

	dir = File.join(RAILS_ROOT,'private')
	
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
end
