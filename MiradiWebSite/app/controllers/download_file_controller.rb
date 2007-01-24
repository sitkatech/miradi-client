$available_files = ['README']

class DownloadFileController < ApplicationController
  before_filter :login_required

  def index
	file = params[:file]
	if(!$available_files.index(file))
	  redirect_to :action => "download" 
	end
	type = 'application/octet-stream'
	if(file == 'README')
	  type = 'text'
	end
	send_file('private/' + file, {:stream => false, :type => type})
  end
end
