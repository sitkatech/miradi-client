# Filters added to this controller apply to all controllers in the application.
# Likewise, all the methods added will be available for all controllers.

class ApplicationController < ActionController::Base
  # Pick a unique cookie name to distinguish our session data from others'
  session :session_key => '_miradi.org_session_id'
  layout 'application'

  def login_required
    if session[:user]
      return true
    end
    flash[:warning]='Please login to continue'
    redirect_to :controller => "download"
    return false 
  end
  
  def admin_required
    if session[:user] && session[:user].admin?
      return true
    end
    redirect_to :controller => "welcome"
    return false 
  end
end
