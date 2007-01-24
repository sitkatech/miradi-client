class LogoutController < ApplicationController
  def index
    session[:user] = nil
    redirect_to '/login'
  end
end
