<template>
  <div class="flex flex-row justify-center items-center h-screen w-screen">

    <div class="flex justify-center items-center w-3/5 h-full">
      <img src="/redirect.svg" alt="loading" class="w-1/2 h-1/2" />
    </div>

    <div class="flex flex-col justify-center items-center w-2/5 h-full">
      <div class="w-fit flex flex-col gap-2">
        <Title class="text-7xl">Oups...</Title>
        <p class="font-CASlalomRegular">Il semble que vous ayez été déconnecté !</p>
        <br><br>
        <Button class="w-full py-4" @click="redirect">Retour à la page de connexion</Button>
      </div>
    </div>
  </div>
</template>

<script setup>

async function redirect() {
  await router.push('/login');
}

</script>

<script>
import Button from '../components/Buttons/Button.vue';
import Title from '../components/Title.vue';
import router from '../router/base';
import Cookies from 'js-cookie';

/**
 * Cette Vue permet de rediriger l'utilisateur vers la page de connexion
 * après avoir supprimé le token de l'utilisateur.
 */
export default {
  name: 'RedirectView',
  mounted() {
    Cookies.remove('token');
  },
};
</script>