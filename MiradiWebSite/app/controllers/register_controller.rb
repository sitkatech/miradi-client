class RegisterController < ApplicationController
  def attempt
    if request.post?
  	  email = params[:user][:email]
  	  password = params[:user][:password]
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
