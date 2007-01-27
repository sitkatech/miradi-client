class RenameCreatedTimestamp < ActiveRecord::Migration
  def self.up
    remove_column :users, :created_timestamp
    add_column :users, :created_at, :datetime
  end

  def self.down
    add_column :users, :created_timestamp, :datetime
    remove_column :users, :created_at
  end
end
