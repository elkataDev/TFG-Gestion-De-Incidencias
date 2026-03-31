FROM node:20-alpine
WORKDIR /app

# Enable pnpm
RUN corepack enable

# Copy dependencies first for better caching
COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile

# Copy src
COPY . .

EXPOSE 5173
CMD ["pnpm", "run", "dev", "--host"]
