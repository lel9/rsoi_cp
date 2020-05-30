FROM nginx:1.15.2-alpine
COPY ./build /var/www
COPY ./nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

# Copy .env file and shell script to container
WORKDIR /var/www
COPY ./env.sh .
COPY ./.env .

# Add bash
RUN apk add --no-cache bash

# Make our shell script executable
RUN chmod +x env.sh

CMD ["/bin/bash", "-c", "/var/www/env.sh && nginx -g \"daemon off;\""]