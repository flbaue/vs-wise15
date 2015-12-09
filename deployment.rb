#!/usr/bin/env ruby
require 'fileutils'


deployment_root = "/Users/florian/HAW ownCloud/VS/VSP_aaz532"
repository_root = Dir.pwd


services = {
  "DiceService": "docker_0",
  "GamesService": "docker_1"
}

services.each_pair do |service, docker_folder|
  puts "\n## Building #{service}"

  puts "debug: #{service}"
  puts "debug: #{docker_folder}"

  project_path = "#{repository_root}/#{service}"
  deployment_path = "#{deployment_root}/#{docker_folder}/java"


  Dir.chdir project_path
  result = `./gradlew clean fatJar`
  puts result

  puts "\n## Deploying #{service}"
  source_path = "#{project_path}/build/libs/#{service}-all-1.0.jar"
  dest_path = "#{deployment_root}/#{deployment_path}"

  Dir.chdir deployment_root
  FileUtils.rm_rf(".#{deployment_path}")
  FileUtils.mkdir_p(dest_path)
  FileUtils.cp(source_path, "#{dest_path}/vsp_aaz532.jar")

  puts "## #{service} Done."
end
