# This script installs the 'mill' build tool in your /usr/local/bin/mill
sudo sh -c '(echo "#!/usr/bin/env sh" && curl -L https://github.com/lihaoyi/mill/releases/download/0.3.6/0.3.6) > /usr/local/bin/mill && chmod +x /usr/local/bin/mill'