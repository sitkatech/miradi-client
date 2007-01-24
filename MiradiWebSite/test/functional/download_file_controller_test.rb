require File.dirname(__FILE__) + '/../test_helper'
require 'download_file_controller'

# Re-raise errors caught by the controller.
class DownloadFileController; def rescue_action(e) raise e end; end

class DownloadFileControllerTest < Test::Unit::TestCase
  def setup
    @controller = DownloadFileController.new
    @request    = ActionController::TestRequest.new
    @response   = ActionController::TestResponse.new
  end

  # Replace this with your real tests.
  def test_truth
    assert true
  end
end
