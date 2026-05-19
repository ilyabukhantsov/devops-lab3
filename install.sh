#!/bin/bash
set -e

echo "--- [1/6] Updating system and installing Docker & Nginx ---"
sudo apt update && sudo apt upgrade -y
sudo apt install -y nginx sudo curl git

if ! command -v docker &>/dev/null; then
  curl -fsSL https://get.docker.com -o get-docker.sh
  sudo sh get-docker.sh
  rm get-docker.sh
fi

echo "--- [2/6] Creating users ---"

create_user_safe() {
  local username=$1
  if id "$username" &>/dev/null; then
    echo "User '$username' already exists, skipping..."
  else
    if getent group "$username" &>/dev/null; then
      sudo groupdel "$username" 2>/dev/null || true
    fi

    if [ "$username" = "app" ]; then
      sudo useradd -r -m -d /home/app -s /usr/sbin/nologin app
    else
      sudo useradd -m -s /bin/bash "$username"
      echo "$username:12345678" | sudo chpasswd
    fi
    echo "User '$username' created successfully."
  fi
}

create_user_safe "app"
create_user_safe "student"
create_user_safe "teacher"
create_user_safe "operator"

id -nG student | grep -q "\bsudo\b" || sudo usermod -aG sudo student
id -nG teacher | grep -q "\bsudo\b" || sudo usermod -aG sudo teacher

sudo chage -d 0 teacher 2>/dev/null || true
sudo chage -d 0 operator 2>/dev/null || true

echo "--- [3/6] Starting PostgreSQL Container ---"
sudo docker run -d \
  --name notes-db \
  --restart always \
  -e POSTGRES_DB=notes_db \
  -e POSTGRES_USER=app_user \
  -e POSTGRES_PASSWORD=secure_password \
  -p 5432:5432 \
  postgres:16-alpine

echo "--- [4/6] Configuring Systemd Unit for App Container ---"
cat <<EOF | sudo tee /etc/systemd/system/mywebapp.service
[Unit]
Description=Notes Service Container (KPI Lab 3)
After=network.target

[Service]
Restart=always
RestartSec=5
ExecStartPre=-/usr/bin/docker rm -f mywebapp-app
ExecStart=/usr/bin/docker run --rm --name mywebapp-app \\
    -p 5200:8080 \\
    -e SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/notes_db \\
    -e SPRING_DATASOURCE_USERNAME=app_user \\
    -e SPRING_DATASOURCE_PASSWORD=secure_password \\
    ghcr.io/ilyabukhantsov/devops/notes-service:latest

ExecStop=/usr/bin/docker stop mywebapp-app

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable mywebapp.service

echo "--- [5/6] Configuring Nginx (Reverse Proxy) ---"
cat <<EOF | sudo tee /etc/nginx/sites-available/mywebapp
server {
    listen 80;
    server_name _;

    location / {
        proxy_pass http://127.0.0.1:5200;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        
        proxy_connect_timeout 10s;
        proxy_read_timeout 60s;
    }
}
EOF

sudo ln -sf /etc/nginx/sites-available/mywebapp /etc/nginx/sites-enabled/
sudo rm -f /etc/nginx/sites-enabled/default
sudo systemctl restart nginx

echo "--- [6/6] Configuring sudoers for operator ---"
cat <<EOF | sudo tee /etc/sudoers.d/operator-rules
operator ALL=(ALL) NOPASSWD: /usr/bin/systemctl start mywebapp.service
operator ALL=(ALL) NOPASSWD: /usr/bin/systemctl stop mywebapp.service
operator ALL=(ALL) NOPASSWD: /usr/bin/systemctl restart mywebapp.service
operator ALL=(ALL) NOPASSWD: /usr/bin/systemctl status mywebapp.service
operator ALL=(ALL) NOPASSWD: /usr/sbin/nginx -s reload
EOF
sudo chmod 0440 /etc/sudoers.d/operator-rules

mkdir -p /home/student
echo "6" | sudo tee /home/student/gradebook
sudo chown student:student /home/student/gradebook

echo "-------------------------------------------------------"
echo "TARGET NODE ENVIRONMENT IS READY!"
echo "-------------------------------------------------------"
