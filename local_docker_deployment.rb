#!/usr/bin/env ruby
require 'fileutils'

class Service
  attr_accessor :folder
  attr_accessor :jar_name
  attr_accessor :docker_port
  attr_accessor :docker_image
  attr_accessor :docker_container

  def initialize(folder, jar_name, docker_port, docker_image, docker_container)
    @folder = folder
    @jar_name = jar_name
    @docker_port = docker_port
    @docker_image = docker_image
    @docker_container = docker_container
  end
end

buildList = []
ARGV.each do|a|
  #puts "Argument: #{a}"
  buildList << a
end

project_folder = File.expand_path(File.dirname(__FILE__))
jar_folder = "build/libs"

games_service = Service.new("GamesService", "GamesService-all-1.0.jar", 4502, "superteam/games-service", "GAMESSERVICE")
player_service = Service.new("PlayerService", "PlayerService-all-1.0.jar", 4500, "superteam/player-service", "PLAYERSERVICE")
boards_service = Service.new("boards_service", "boards_service-all-1.0.jar", 4501, "superteam/boards-service", "BOARDSSERVICE")
dice_service = Service.new("DiceService", "DiceService-all-1.0.jar", 4503, "superteam/dice-service", "DICESERVICE")
banks_service = Service.new("banks_service", "banks-all-1.0.jar", 4504, "superteam/banks-service", "BANKSSERVICE")
banks_service2 = Service.new("banks_service", "banks-all-1.0.jar", 4507, "superteam/banks-service-2", "BANKSSERVICE2")
broker_service = Service.new("brokers", "brokers-all-1.0.jar", 4505, "superteam/brokers-service", "BROKERSSERVICE")
events_service = Service.new("events_service", "events_service-all-1.0.jar", 4506, "superteam/events-service", "EVENTSSERVICE")
haproxy_service = Service.new("HAProxy","haproxy.cfg", 4599, "superteam/haproxy", "HAPROXY")
#jail_service = Service.new("JailService", "JailService-all-1.0.jar", 4507, "superteam/jail-service", "JAILSERVICE")
#...

failed = []

services = [player_service, boards_service, games_service, dice_service, banks_service, banks_service2, broker_service, events_service, haproxy_service]
services.each do |service|

  if buildList.size > 0
    next if !buildList.include?(service.folder)
  end

  # 1. Build fat jar
  puts "## Building #{service.folder}"
  service_path = "#{project_folder}/#{service.folder}"
  Dir.chdir service_path

  if service.folder != "HAProxy"
    result = `./gradlew clean fatJar`
    if !result.include?('BUILD SUCCESSFUL')
      puts result
      puts "## Building Failed"
      puts ""
      failed << service
      next
    else
      puts "## Building Succeeded"
    end
  end

  # 2. copy jar to docker folder
  puts "## Copying Jar to Docker folder"
  jar_path = "#{service_path}/#{jar_folder}/#{service.jar_name}" if service.folder != "HAProxy"
  jar_path = "#{service_path}/#{service.jar_name}" if service.folder == "HAProxy"
  docker_path = "#{project_folder}/docker/#{service.folder}"
  FileUtils.rm_rf(docker_path)
  FileUtils.mkdir_p(docker_path)
  FileUtils.cp(jar_path, docker_path)

  # 3. create Dockerfile
  puts "## Creating Dockerfile"
  Dir.chdir docker_path
  if service.folder != "HAProxy"
    File.open("Dockerfile", 'w') do |f|
      f.write("FROM java:8\n")
      f.write("COPY . /usr/local\n")
      f.write("WORKDIR /usr/local\n")
      f.write("CMD [\"java\", \"-jar\", \"#{service.jar_name}\"]\n")
      f.write("EXPOSE 4567\n")
    end
  else
    File.open("Dockerfile", 'w') do |f|
      f.write("FROM haproxy:1.5\n")
      f.write("COPY haproxy.cfg /usr/local/etc/haproxy/haproxy.cfg\n")
      f.write("EXPOSE 4567\n")
    end
  end

  # 4. create docker image
  puts "## Stopping old container"
  result = `docker stop #{service.docker_container}`
  puts "## Removing old container"
  result = `docker rm #{service.docker_container}`
  puts "## Removing old image"
  result = `docker rmi #{service.docker_image}`
  #puts result

  puts "## Building new Docker image"
  result = `docker build -t #{service.docker_image} .`
  if !result.include?("Successfully")
    puts "## Failed"
    puts ""
    failed << service
    next
  else
    puts "## New Image #{service.docker_image} build successfully"
  end

  puts "## Starting new container"
  result = `docker run -d -p 192.168.99.100:#{service.docker_port}:4567 --name #{service.docker_container} -t #{service.docker_image}`
  puts result
  if result.include?("Error")
    puts ""
    failed << service
    next
  end
  puts "## #{service.folder} done."
  puts ""
end

if failed.size > 0
  puts "Failed services:"
  failed.each do |service|
    puts service.folder
  end
  puts ""
  puts "Retry with 'ruby local_docker_deployment.rb {service_folder_1}, {service_folder_2} ..."
end
