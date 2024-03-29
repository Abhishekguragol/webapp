packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = ">= 1"
    }
  }
}

variable "project" {
  type    = string
  default = "csye6225-dev-414923"
}

variable "region" {
  type    = string
  default = "us-east1"
}

variable "zone" {
  type    = string
  default = "us-east1-b"
}

variable "source_image" {
  type    = string
  default = "centos-stream-8" # centos stream image for CentOS Stream release 8
}

variable "disk_size" {
  type    = number
  default = 20
}

variable "disk_type" {
  type    = string
  default = "pd-standard"
}


variable "ssh_username" {
  type    = string
  default = "packer"
}

variable "image_family" {
  type    = string
  default = "csye6225-app-image"
}

variable "image_name" {
  type    = string
  default = "csye6225-{{timestamp}}"
}

variable "image_description" {
  type    = string
  default = "CSYE 6225 App Custom Image"
}

variable "image_storage_locations" {
  type    = list(string)
  default = ["us"]
}

source "googlecompute" "csye6225-app-custom-image" {
  project_id              = var.project
  source_image_family     = var.source_image
  region                  = var.region
  zone                    = var.zone
  disk_size               = var.disk_size
  disk_type               = var.disk_type
  image_name              = var.image_name
  image_description       = var.image_description
  image_family            = var.image_family
  image_project_id        = var.project
  image_storage_locations = var.image_storage_locations
  ssh_username            = var.ssh_username


}

build {
  sources = [
    "sources.googlecompute.csye6225-app-custom-image",
  ]

  provisioner "file" {
    source      = "./target/project-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/"
  }

  provisioner "file" {
    source      = "./services/appstart.service"
    destination = "/tmp/"
  }
  provisioner "shell" {
    scripts = [
      "./scripts/user.sh",
      "./scripts/base-setup.sh",
      "./scripts/services.sh",
    "./scripts/agent-setup.sh"]
  }
}
