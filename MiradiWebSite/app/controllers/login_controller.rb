class LoginController < ApplicationController
  def login
	render :action => 'index'
  end
  
  def attempt_login
  	if request.post?
      email = params[:user][:email]
      password = params[:user][:password]
      
  	  u = User.authenticate(email, password)
      if u
        session[:user] = u 
        flash[:message]  = "Login successful"
      else
        flash[:warning] = "Login unsuccessful"
      end
      if session[:user]
	    redirect_to "/download"
	  else
	    render :action => 'index'
	  end
    end
    
  end

  def logout
    session[:user] = nil
    render :action => 'index'
  end

  def attempt_register
    if request.post?
  	  email = params[:user][:email]
  	  password = params[:user][:password]
  	  access_code = params[:user][:access_code]
  	  
  	  ac = AccessCode.find(:first, :conditions=>["code = ?", access_code])
  	  if User.count > 0 && !ac
  	    session[:user] = nil
  	    flash[:warning] = "Unknown Access Code"
        render :action => 'register'
  	    return
  	  end
  	  
      @user = User.new(@params[:user])
      if User.count == 0
        @user.admin_flag = true
      end
      
      if @user.save
        session[:user] = User.authenticate(email, password)
        flash[:message] = "Registration successful"
        redirect_to "/download"          
      else
        session[:user] = nil
        flash[:warning] = "Registration unsuccessful"
        render :action => 'register'
      end
  
    end
  end
end
