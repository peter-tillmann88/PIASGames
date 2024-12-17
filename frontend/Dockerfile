FROM node:20-alpine AS builder

WORKDIR /app

ARG VITE_API_URL
ARG VITE_IMAGE_SERVER_URL

ENV VITE_API_URL=$VITE_API_URL
ENV VITE_IMAGE_SERVER_URL=$VITE_IMAGE_SERVER_URL

COPY package.json package-lock.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /app/dist /usr/share/nginx/html

COPY .nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]