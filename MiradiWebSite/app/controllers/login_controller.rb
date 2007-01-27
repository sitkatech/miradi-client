class LoginController < ApplicationController
  def login
	render :action => 'index'
  end
  
  def requestaccess
  end
  
  def registerthanks
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
	    redirect_to :controller => 'files'
	  else
	    redirect_to :back
	  end
    end
    
  end

  def logout
    session[:user] = nil
    redirect_to :back
  end

  def attempt_register
    if request.post?
  	  email = params[:user][:email]
  	  password = params[:user][:password]
  	  access_code = params[:user][:access_code]
  	  
      @user = User.new(@params[:user])
      if User.count == 0
        @user.admin_flag = true
      end
      
      if @user.save
        session[:user] = User.authenticate(email, password)
        flash[:message] = "Registration successful"
        redirect_to :action => 'registerthanks'          
      else
        session[:user] = nil
        flash[:warning] = "Registration unsuccessful"
        render :action => 'register'
      end
  
    end
  end
end
