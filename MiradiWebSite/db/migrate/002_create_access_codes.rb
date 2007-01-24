class CreateAccessCodes < ActiveRecord::Migration
  def self.up
    create_table :access_codes do |t|
      t.column :organization, :string
      t.column :code, :string
    end
  end

  def self.down
    drop_table :access_codes
  end
end
