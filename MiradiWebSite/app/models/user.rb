class User < ActiveRecord::Base
  def self.authenticate(email, pass)
    u=find(:first, :conditions=>["email = ?", email])
    if u.nil?
      return nil
    end
#   if User.encrypt(pass, u.password_salt)==u.password_hash
      return u 
#   end
    
    nil
  end  
  
  
  def self.encrypt(pass, salt)
    Digest::SHA1.hexdigest(pass+salt)
  end
end
