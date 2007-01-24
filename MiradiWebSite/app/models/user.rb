class User < ActiveRecord::Base
  attr_accessor :password
  attr_accessor :password_confirmation

  validates_uniqueness_of :email
  validates_format_of :email, :with => /^([^@\s]+)@((?:[-a-z0-9]+\.)+[a-z]{2,})$/i, :message => "Invalid email"  
  validates_length_of :password, :within => 5..40
  validates_presence_of :email, :password, :password_confirmation, :password_salt
  validates_confirmation_of :password

  
  
  def admin?
    return self.admin_flag
  end

  def password=(pass)
    @password = pass
    if !(self.password_salt?)
      self.password_salt = User.random_string(10)
    end
    self.password_hash = User.encrypt(@password, self.password_salt)
  end


  def self.authenticate(email, pass)
    u=find(:first, :conditions=>["email = ?", email])
    if u.nil?
      return nil
    end
   if User.encrypt(pass, u.password_salt)==u.password_hash
      return u 
   end
    
    nil
  end  
  
  
  def self.encrypt(pass, salt)
    Digest::SHA1.hexdigest(pass+salt)
  end
  
  def self.random_string(len)
    #generate a random password consisting of strings and digits
    chars = ("a".."z").to_a + ("A".."Z").to_a + ("0".."9").to_a
    newpass = ""
    1.upto(len) { |i| newpass << chars[rand(chars.size-1)] }
    return newpass
  end
end
