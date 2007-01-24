class CreateUsers < ActiveRecord::Migration
  def self.up
    create_table :users do |t|
		t.column :email, :string, :limit=>80
		t.column :password_hash, :string, :limit=>80
		t.column :password_salt, :string, :limit=>80
		t.column :organization, :string, :limit=>80
		t.column :position, :string, :limit=>80
		t.column :created_timestamp, :datetime
		t.column :access_code, :string, :limit=>80
		t.column :notes, :string, :limit=>500
    end
  end

  def self.down
    drop_table :users
  end
end
