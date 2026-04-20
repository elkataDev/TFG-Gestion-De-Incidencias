FROM node:20-alpine
WORKDIR /app

# Copy dependencies first for better caching
COPY package.json package-lock.json ./
RUN npm ci

# Copy src
COPY . .

EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host"]
