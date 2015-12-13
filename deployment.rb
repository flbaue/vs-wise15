#!/usr/bin/env ruby
require 'fileutils'

# Config

## Change paths according to your setup
deployment_root = "/Users/florian/HAW ownCloud/VS/VSP_aaz532"
repository_root = Dir.pwd

## Add services with thier name and docker folder
## Services must have fatJar task in thier build.gradle file!
services = {
  "DiceService": "docker_0",
  "GamesService": "docker_1"
}

# Config End

services.each_pair do |service, docker_folder|
  puts "\n## Building #{service}"

  #puts "debug: #{service}"
  #puts "debug: #{docker_folder}"

  project_path = "#{repository_root}/#{service}"
  deployment_path = "#{docker_folder}/java"


  Dir.chdir project_path
  result = `./gradlew clean fatJar`
  if !result.include?('BUILD SUCCESSFUL')
    puts result
    puts "## Building #{service} Failed"
    exit
  else
    puts "## Building #{service} Succeeded"
  end

  puts "## Deploying #{service}"
  source_path = "#{project_path}/build/libs/#{service}-all-1.0.jar"
  dest_path = "#{deployment_root}/#{deployment_path}"

  Dir.chdir deployment_root
  FileUtils.rm_rf(deployment_path)
  FileUtils.mkdir_p(deployment_path)
  FileUtils.cp(source_path, "./#{deployment_path}/vsp_aaz532.jar")

  puts "## #{service} Done."

end
