<template>
    <router-link
      :to="{ name: route, params: teamId !== null ? { teamId } : etudiantId !== null ? { etudiantId } : {} }"
      v-slot="{ isExactActive }"
      class="flex items-center w-full"
    >
      <div :class="{
        'flex items-center sidebar-element w-full text-white hover:bg-custom-sideBarActiveBG ease-in-out duration-200 cursor-pointer rounded-md': true,
        'bg-custom-sideBarActiveBG': isExactActive
      }">
        <component :is="getComponentByRouteName(name)" class="h-10 w-10 p-2" />
        <p class="text-sm mr-10">{{ name }}</p>
      </div>
    </router-link>
  </template>

  <script setup>
  import { LayoutDashboard, Combine, Users, BookCheck, PersonStanding, UserCog, FileDigit, Bug, ListOrdered, UserPlus, PackagePlus, AlignHorizontalDistributeCenter, Diff, MessageCircleQuestion, Check, GraduationCap } from 'lucide-vue-next';
  import { defineProps } from 'vue';

  const props = defineProps({
    name: String,
    route: String,
    teamId: String,
    etudiantId: Number
  });

  function getComponentByRouteName(routeName) {
    const components = {
      'Tableau de bord': LayoutDashboard,
      'Équipes': Users,
      'Notes': BookCheck,
      'Gestion des notes': BookCheck,
      'Étudiants': PersonStanding,
      'Gestion des étudiants': UserCog,
      'Évaluer un sprint': FileDigit,
      'Test': Bug,
      'Établir un ordre': ListOrdered,
      'Gestion des équipes': Combine,
      'Gestion des sprints': PackagePlus,
      'Gestion des échelles': AlignHorizontalDistributeCenter,
      'Attribuer des bonus/malus': Diff,
      'Bonus/malus de mon équipe': Check,
      'Questions / Réponses': MessageCircleQuestion,
      'Valider des bonus/malus': Check,
      "Création d'échelles": GraduationCap,
    };
    return components[routeName];
  }
  </script>
