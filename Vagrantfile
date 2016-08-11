# -*- mode: ruby -*-
# vi: set ft=ruby :
#
# Author: Zak Hassan
#

  Vagrant.configure(2) do |config|
    config.vm.box = "bento/centos-7.1"
    config.vm.provider "virtualbox" do |v|
        v.memory = 1024
      end
    config.vm.define "metadata-vagrant" do |d|
         d.vm.provision :shell, path: "bootstrap.sh"
         d.vm.hostname = "metadata.vagrant.com"
         d.vm.network "private_network", ip: "192.168.33.12"
    end
end
