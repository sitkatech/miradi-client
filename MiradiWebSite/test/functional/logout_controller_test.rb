require File.dirname(__FILE__) + '/../test_helper'
require 'logout_controller'

# Re-raise errors caught by the controller.
class LogoutController; def rescue_action(e) raise e end; end

class LogoutControllerTest < Test::Unit::TestCase
  def setup
    @controller = LogoutController.new
    @request    = ActionController::TestRequest.new
    @response   = ActionController::TestResponse.new
  end

  # Replace this with your real tests.
  def test_truth
    assert true
  end
end
