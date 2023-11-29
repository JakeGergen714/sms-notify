# Stage 1: Build
FROM node:alpine as build
WORKDIR /app
COPY ./package.json ./package-lock.json ./
RUN npm install
COPY . ./
RUN npm run build

# Stage 2: Serve
FROM node:alpine
WORKDIR /app
COPY --from=build /app/build .
RUN npm install -g serve
CMD ["serve", "-s", "."]
EXPOSE 80