class RegisterController < ApplicationController
  def attempt
    if request.post?
  	  email = params[:user][:email]
  	  password = params[:user][:password]
  	  access_code = params[:user][:access_code]
  	  
  	  ac = AccessCode.find(:first, :conditions=>["code = ?", access_code])
  	  if !ac
  	    session[:user] = nil
  	    flash[:warning] = "Unknown Access Code"
  	    redirect_to :action => 'index'
  	    return
  	  end
  	  
      @user = User.new(@params[:user])
      if @user.save
        session[:user] = User.authenticate(email, password)
        flash[:message] = "Registration successful"
        redirect_to "/download"          
      else
        session[:user] = nil
        flash[:warning] = "Registration unsuccessful"
        redirect_to "/register"
      end
  
    end
  end
end
