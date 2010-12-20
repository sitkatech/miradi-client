#!/usr/bin/env ruby

require 'rubygems'
require 'ramaze'

# Initialize controllers and models
require 'controller/init'
require 'model/init'

adapter = :thin
host = '0.0.0.0'
port = 7000
site = File.dirname(File.expand_path(__FILE__))
puts "#{site} Listening on #{host}:#{port}"
Ramaze.start(:adapter => adapter, :host => host, :port => port, :file => __FILE__)
