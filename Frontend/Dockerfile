FROM node:20-alpine AS build
WORKDIR /app

# Enable pnpm
RUN corepack enable

# Copy dependencies first for better caching
COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile

# Copy src and build
COPY . .
RUN pnpm run build

# Serve stage
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html

# Custom nginx config to support React Router (SPA fallback)
RUN echo -e 'server {\n    listen 80;\n\n    location / {\n        root   /usr/share/nginx/html;\n        index  index.html index.htm;\n        try_files $uri $uri/ /index.html;\n    }\n}' > /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
