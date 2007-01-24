require File.dirname(__FILE__) + '/../test_helper'
require 'attempt_login_controller'

# Re-raise errors caught by the controller.
class AttemptLoginController; def rescue_action(e) raise e end; end

class AttemptLoginControllerTest < Test::Unit::TestCase
  def setup
    @controller = AttemptLoginController.new
    @request    = ActionController::TestRequest.new
    @response   = ActionController::TestResponse.new
  end

  # Replace this with your real tests.
  def test_truth
    assert true
  end
end
