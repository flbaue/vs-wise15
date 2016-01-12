#!/usr/bin/env ruby
require 'fileutils'
require 'set'

# Config

## Change paths according to your setup
deployment_root = "/Users/florian/HAW ownCloud/VS/VSP_aaz532"
repository_root = Dir.pwd

## Add services with thier name and docker folder
## Services must have fatJar task in thier build.gradle file!
services = {
  "docker_0" => {folder: "DiceService", jar: "DiceService-all-1.0.jar"},
  "docker_1" => {folder: "GamesService", jar: "GamesService-all-1.0.jar"},
  "docker_2" => {folder: "banks_service", jar: "banks-all-1.0.jar"},
  "docker_3" => {folder: "banks_service", jar: "banks-all-1.0.jar"},
  "docker_4" => {folder: "boards_service", jar: "boards_service-all-1.0.jar"},
  "docker_5" => {folder: "events_service", jar: "events_service-all-1.0.jar"}
}

# Config End

buildSet = Set.new
ARGV.each do|a|
  #puts "Argument: #{a}"
  buildSet.add(a)
end

services.each_pair do |docker_folder, properties|

  service = properties[:folder]
  jar_name = properties[:jar]

  if buildSet.size > 0
    next if !buildSet.include? service
  end

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
  source_path = "#{project_path}/build/libs/#{jar_name}"
  dest_path = "#{deployment_root}/#{deployment_path}"

  Dir.chdir deployment_root
  FileUtils.rm_rf(docker_folder)
  FileUtils.mkdir_p(deployment_path)
  FileUtils.cp(source_path, "./#{deployment_path}/vsp_aaz532.jar")

  puts "## #{service} Done."

end
