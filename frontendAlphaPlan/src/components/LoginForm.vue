<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Cookies from 'js-cookie';
import { login, getTeamReferent } from '../services/JwtService';
import { AtSign } from 'lucide-vue-next';
import { KeyRound } from 'lucide-vue-next';
import Eseo from '../assets/icons/Eseo.vue';

let email = ref('');
let password = ref('');
let errorMessage = ref('');
const router = useRouter();

const defineTeamId = async (data) => {
  //console.log(localStorage.getItem('roles').includes('SUPERVISING_STAFF'));
  if (localStorage.getItem('roles').includes('SUPERVISING_STAFF')) {
    try {
      const equipeRef = await getTeamReferent(data.id);
      if (equipeRef != null) {
        localStorage.setItem('teamId', equipeRef.id);
        return;
      }
    } catch (error) {}
  }
  localStorage.setItem('teamId', data.teamId);
};

const formSubmit = async () => {
  // Default border color
  document.getElementById('mail').style.borderColor = '#9ca3af';
  document.getElementById('password').style.borderColor = '#9ca3af';

  // Send the data to the backend
  login(password.value, email.value)
  .then(async function (data) {
      errorMessage.value = '';
      // Default border color
      document.getElementById('mail').style.borderColor = 'green';
      document.getElementById('password').style.borderColor = 'green';

      //Save User information in localStorage
      localStorage.setItem('token', data.access_token);
      localStorage.setItem('user_id', data.id);
      localStorage.setItem('roles', JSON.stringify(data.roles.map(roles => roles.role.nom)));
      localStorage.setItem('prenom', data.prenom);
      localStorage.setItem('nom', data.nom);
      await defineTeamId(data);
      console.log("teamId: ", localStorage.getItem('teamId'));

      // Redirect to the home page
      router.push('/homepage');
    })
    .catch(function (error) {
      errorMessage.value = "Erreur de connexion. Veuillez vérifier vos identifiants.";
      console.log(error);
      // Change the border color of the input fields
      let bdColor = 'red';
      document.getElementById('mail').style.borderColor = bdColor;
      document.getElementById('password').style.borderColor = bdColor;
    });
}
</script>

<template>
  <div :id="'login-form'"
    class="grid grid-rows-4 grid-flow-col gap-4 w-[600px] h-fit items-center text-[20px] font-['ABeeZeeRegular']">

    <div>
      <div class="flex gap-[.5vw] w-[100%] justify-start items-center text-[#1B1336]">
        <AtSign />
        <p>Adresse mail</p>
      </div>
      <input
        class="px-3 py-2 h-[55px] bg-white border shadow-sm border-slate-200 placeholder-slate-400 focus:outline-none focus:border-sky-500 focus:ring-sky-500 block w-full rounded-md sm:text-sm focus:ring-1 contrast-more:border-slate-400 contrast-more:placeholder-slate-500"
        id="mail" type="text" v-model="email" placeholder="prenom.nom@reseau.eseo.fr">
    </div>

    <div>
      <div class="flex gap-[.5vw] w-[100%] justify-start items-center text-[#1B1336]">
        <KeyRound />
        <p>Mot de passe</p>
      </div>
      <input
        class="px-3 py-2 h-[55px] bg-white border shadow-sm border-slate-200 placeholder-slate-400 focus:outline-none focus:border-sky-500 focus:ring-sky-500 block w-full rounded-md sm:text-sm focus:ring-1 contrast-more:border-slate-400 contrast-more:placeholder-slate-500"
        id="password" type="password" v-model="password" placeholder="Votre mot de passe" @keydown.enter="formSubmit">
    </div>

    <div class="flex flex-row justify-between items-center">
      <div class="bg-slate-300 w-[40%] h-[1px]"></div>
      <Eseo :width="30" :height="30" :color="'rgb(203,213,225)'"/>
      <div class="bg-slate-300 w-[40%] h-[1px]"></div>
    </div>

    <div class="flex flex-col items-center row-start-4">
      <button class="w-[600px] h-[55px] bg-[#1B1336] hover:bg-[#30A1EF] text-white rounded-md ease-in-out duration-300" @click="formSubmit">Se
        connecter
      </button>
      <div class="flex items-center h-[10px] my-[10px] text-red-400 text-[15px] italic">{{ errorMessage }}</div>
    </div>
  </div>
</template>