class UserSplitNameAddStateCountry < ActiveRecord::Migration
  def self.up
    remove_column :users, :name
    add_column :users, :first_name, :string, :limit=>40
    add_column :users, :last_name, :string, :limit=>40
    add_column :users, :state, :string, :limit=>40
    add_column :users, :country, :string, :limit=>40
  end

  def self.down
    remove_column :users, :first_name
    remove_column :users, :last_name
    remove_column :users, :state
    remove_column :users, :country
    add_column :users, :name, :string, :limit=>80
  end
end
