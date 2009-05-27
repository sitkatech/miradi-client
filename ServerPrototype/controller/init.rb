# Define a subclass of Ramaze::Controller holding your defaults for all
# controllers

class Controller < Ramaze::Controller
  #layout '/page'
  #helper :xhtml
  engine :None
end

acquire __DIR__/"*.rb"
