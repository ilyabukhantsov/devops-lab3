#!/bin/bash
set -e

echo "--- [1/5] Updating system and installing Docker & Nginx ---"
sudo apt update && sudo apt upgrade -y
sudo apt install -y nginx sudo curl git

if ! command -v docker &>/dev/null; then
  curl -fsSL https://get.docker.com -o get-docker.sh
  sudo sh get-docker.sh
  rm get-docker.sh
fi

echo "--- [2/5] Creating users (student, teacher, operator, app) ---"
create_user_safe() {
  local username=$1
  if id "$username" &>/dev/null; then
    echo "User '$username' already exists, skipping..."
  else
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

sudo usermod -aG sudo student 2>/dev/null || true
sudo usermod -aG sudo teacher 2>/dev/null || true
sudo usermod -aG docker $USER 2>/dev/null || true

echo "--- [3/5] Starting MariaDB Container (Matches application.properties) ---"
if ! sudo docker ps -a | grep -q mariadb; then
    sudo docker run -d \
      --name mariadb \
      -p 3306:3306 \
      -e MARIADB_ROOT_PASSWORD=kpi_secret_pass \
      -e MARIADB_DATABASE=notes_db \
      --restart always \
      mariadb:latest
fi

echo "--- [4/5] Configuring Nginx (Reverse Proxy to port 8080) ---"
cat <<EOF | sudo tee /etc/nginx/sites-available/mywebapp
server {
    listen 80;
    server_name _;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        
        proxy_connect_timeout 10s;
        proxy_read_timeout 60s;
    }
}
EOF

sudo ln -sf /etc/nginx/sites-available/mywebapp /etc/nginx/sites-enabled/
sudo rm -f /etc/nginx/sites-enabled/default
sudo systemctl restart nginx

echo "--- [5/5] Configuring sudoers for operator ---"
cat <<EOF | sudo tee /etc/sudoers.d/operator-rules
operator ALL=(ALL) NOPASSWD: /usr/bin/docker restart app
operator ALL=(ALL) NOPASSWD: /usr/bin/docker status app
operator ALL=(ALL) NOPASSWD: /usr/sbin/nginx -s reload
EOF
sudo chmod 0440 /etc/sudoers.d/operator-rules

echo "-------------------------------------------------------"
echo "TARGET NODE ENVIRONMENT IS TUNED AND READY!"
echo "-------------------------------------------------------"