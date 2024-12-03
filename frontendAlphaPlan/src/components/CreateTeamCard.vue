<template>
    <div v-if="team" class="border rounded-md shadow-sm p-4 mb-8 flex flex-col w-full lg:w-[49%] 2xl:w-[32%] bg-white border-gray-200">
      <div class="p-1">
        <input ref="teamNameInput" :value="computedTeamName" @input="updateTeamName" class="text-lg font-bold italicPlaceholder w-full focus:border-custom-darkblueAP" type="text" :placeholder="(teamName === null) ? 'Équipe ' + (index+1) : teamName">
        <select v-model="selectedStaffId" @change="updateTeamStaff" class="block w-full bg-white border border-gray-300 rounded-md py-2 px-3 pr-8 focus:outline-none focus:border-custom-darkblueAP sm:text-sm mt-2">
          <option value="" selected disabled hidden>-- Staff --</option>
          <option v-for="(encadrant, index) in encadrants" :key="index" :value="encadrant.id">{{ encadrant.prenom }} {{ encadrant.nom }}</option>
        </select>
      </div>
      <div class="flex-grow mt-1">
        <draggable
          class="h-full"
          :list="team.members"
          group="people"
          @update="handleMemberUpdate"
          item-key="id"
        >
          <template #item="{ element }">
            <div class="flex list-group-item cursor-move border-b p-3">
              <img width="20px" src="../assets/images/threelines.svg">
              <div class="w-full ml-3 flex">
                <p>{{ element.prenom }} {{ element.nom }}</p>
                <div class="w-2/12 ml-auto">
                  <p class="">{{ element.moyenne.toFixed(2) }}</p>
                </div>
                <div class="w-1/12 mr-2">
                  <Label v-if="element.typeUtilisateur === 'BACHELOR'" :type="3">B</Label>
                </div>
                <div class="w-1/12">
                  <div class="rounded-lg bg-gray-200 w-6 h-6 flex justify-center">
                    <Label v-if="element.genre === 'FEMME'" :type="0">F</Label>
                    <Label v-else :type="1">H</Label>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </draggable>
      </div>
      <div class="max-h-7 flex w-full mt-4 mb-6">
        <div class="p-2 w-1/4 h-14 rounded-l-xl text-center bg-gray-100 mr-1">
          <p class="text-sm">Membres</p>
          <p class="font-bold">{{ team.members.length }}</p>
        </div>
        <div class="p-2 w-1/4 h-14 text-center bg-gray-100 mr-1">
          <p class="text-sm">Femmes</p>
          <p class="font-bold">{{ getWomanAmount(team.members) }}</p>
        </div>
        <div class="p-2 w-1/4 h-14 text-center bg-gray-100 mr-1">
          <p class="text-sm">Bachelor</p>
          <p class="font-bold">{{ getBachelorAmount(team.members) }}</p>
        </div>
        <div class="p-2 w-1/4 h-14 rounded-r-xl text-center bg-gray-100">
          <p class="text-sm">Moyenne</p>
          <p class="font-bold">{{ getTeamGradesAverage(team.members) }}</p>
        </div>
      </div>
    </div>
  </template>
  
<script setup>
  import { ref, onMounted, watch, computed } from 'vue';
  import draggable from "vuedraggable";
  import { getEncadrants } from '../services/CreerEquipesService';
  import Label from './DataTable/Label.vue';
  
  const props = defineProps({
      team: Object,
      teamName: String,
      teamStaff: Object,
      index: Number
  });
  
  const emit = defineEmits(['update:teamName', 'update:teamStaff']);
  const encadrants = ref([]);
  const selectedStaffId = ref(null);
  const teamNameInput = ref(null);

  const fetchEncadrants = async () => {
    try {
      const response = await getEncadrants();

      encadrants.value = response;
    } catch (error) {
      console.error('Erreur lors de la récupération des encadrants :', error);
    }
  };

  const computedTeamName = computed(() => {
    return props.teamName === null ? `Équipe ${props.index + 1}` : props.teamName;
  });
  
  const updateTeamName = (event) => {
      emit('update:teamName', event.target.value);
  };

  const updateTeamStaff = () => {
    const selectedEncadrant = encadrants.value.find(encadrant => encadrant.id === selectedStaffId.value);

    if (selectedEncadrant) {
      emit('update:teamStaff', selectedEncadrant);
    } else {
      console.warn('Encadrant non trouvé pour l\'identifiant:', selectedStaffId.value);
    }
  };

  watch(() => props.teamStaff, (newVal) => {
    if (newVal) {
      selectedStaffId.value = newVal.id;
    } else {
      selectedStaffId.value = null;
    }
  });
  
  const handleMemberUpdate = (event) => {
      emit('team-statistics-updated', props.team);
  };
  
  const getWomanAmount = (members) => {
      return members.filter(person => person.genre === "FEMME").length;
  };
  
  const getBachelorAmount = (members) => {
      return members.filter(person => person.typeUtilisateur === "BACHELOR").length;
  };
  
  const getTeamGradesAverage = (members) => {
      if (members.length === 0) return 0;
      const totalGrades = members.reduce((acc, member) => acc + parseFloat(member.moyenne), 0);
      return (totalGrades / members.length).toFixed(2);
  };

  onMounted(() => {
    fetchEncadrants();
    if (props.teamStaff) {
      selectedStaffId.value = props.teamStaff.id;
    }
    else{
      selectedStaffId.value = "";
    }
    emit('update:teamName', computedTeamName.value);
  });
</script>

<style scoped>
  .italicPlaceholder::placeholder{
    font-style: italic;
    font-weight: 400;
  }
</style>

  