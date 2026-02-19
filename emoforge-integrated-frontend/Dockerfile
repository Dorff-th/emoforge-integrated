# ----------------------
# 1️⃣ Build Stage
# ----------------------
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install --legacy-peer-deps

COPY . .
ARG VITE_MODE=prod
RUN npm run build:${VITE_MODE}

# ----------------------
# 2️⃣ Nginx Stage
# ----------------------
FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]


