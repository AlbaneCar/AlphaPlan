<script setup>
import { useRouter } from 'vue-router';
import Cookies from 'js-cookie';
import axios from 'axios';
import { LogOut } from 'lucide-vue-next';
const router = useRouter();

const logout = () => {
  // Remove the token from the cookie if it's still there
  if (localStorage.getItem('token')) {
    Cookies.remove('token');
    localStorage.removeItem('token');
  }

  // Send a request to the backend to reset the token in the user's table
  axios.post(import.meta.env.VITE_IP_BACKEND + 'v1/auth/logout');

  // Check if the cookie has been removed
  if (true) {
    // Redirect to the login page
    router.push('/login');
  }
}
</script>

<template>
    <button class="text-white" @click="logout">
        <LogOut class="w-6 hover:text-[#5f42be]"/>
    </button>
</template>