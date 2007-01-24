class AccessCodesController < ApplicationController
  before_filter :admin_required

  def index
    list
    render :action => 'list'
  end

  # GETs should be safe (see http://www.w3.org/2001/tag/doc/whenToUseGet.html)
  verify :method => :post, :only => [ :destroy, :create, :update ],
         :redirect_to => { :action => :list }

  def list
    @access_code_pages, @access_codes = paginate :access_codes, :per_page => 10
  end

  def show
    @access_code = AccessCode.find(params[:id])
  end

  def new
    @access_code = AccessCode.new
  end

  def create
    @access_code = AccessCode.new(params[:access_code])
    if @access_code.save
      flash[:notice] = 'AccessCode was successfully created.'
      redirect_to :action => 'list'
    else
      render :action => 'new'
    end
  end

  def edit
    @access_code = AccessCode.find(params[:id])
  end

  def update
    @access_code = AccessCode.find(params[:id])
    if @access_code.update_attributes(params[:access_code])
      flash[:notice] = 'AccessCode was successfully updated.'
      redirect_to :action => 'show', :id => @access_code
    else
      render :action => 'edit'
    end
  end

  def destroy
    AccessCode.find(params[:id]).destroy
    redirect_to :action => 'list'
  end
end
