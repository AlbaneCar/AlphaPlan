<script setup>
import LoginForm from "../components/LoginForm.vue";
</script>

<template>
  <div :id="'login-body'" class="flex flex-col justify-center items-center h-[100vh] w-[100vw] bg-custom-sideBarBG">
    <img class="h-[200px] w-[200px]" src="../assets/images/logo.svg" alt="AlphaPlan logo">
    <LoginForm />
  </div>
</template>