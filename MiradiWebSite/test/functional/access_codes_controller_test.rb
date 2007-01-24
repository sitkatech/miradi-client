require File.dirname(__FILE__) + '/../test_helper'
require 'access_codes_controller'

# Re-raise errors caught by the controller.
class AccessCodesController; def rescue_action(e) raise e end; end

class AccessCodesControllerTest < Test::Unit::TestCase
  fixtures :access_codes

  def setup
    @controller = AccessCodesController.new
    @request    = ActionController::TestRequest.new
    @response   = ActionController::TestResponse.new

    @first_id = access_codes(:first).id
  end

  def test_index
    get :index
    assert_response :success
    assert_template 'list'
  end

  def test_list
    get :list

    assert_response :success
    assert_template 'list'

    assert_not_nil assigns(:access_codes)
  end

  def test_show
    get :show, :id => @first_id

    assert_response :success
    assert_template 'show'

    assert_not_nil assigns(:access_code)
    assert assigns(:access_code).valid?
  end

  def test_new
    get :new

    assert_response :success
    assert_template 'new'

    assert_not_nil assigns(:access_code)
  end

  def test_create
    num_access_codes = AccessCode.count

    post :create, :access_code => {}

    assert_response :redirect
    assert_redirected_to :action => 'list'

    assert_equal num_access_codes + 1, AccessCode.count
  end

  def test_edit
    get :edit, :id => @first_id

    assert_response :success
    assert_template 'edit'

    assert_not_nil assigns(:access_code)
    assert assigns(:access_code).valid?
  end

  def test_update
    post :update, :id => @first_id
    assert_response :redirect
    assert_redirected_to :action => 'show', :id => @first_id
  end

  def test_destroy
    assert_nothing_raised {
      AccessCode.find(@first_id)
    }

    post :destroy, :id => @first_id
    assert_response :redirect
    assert_redirected_to :action => 'list'

    assert_raise(ActiveRecord::RecordNotFound) {
      AccessCode.find(@first_id)
    }
  end
end
