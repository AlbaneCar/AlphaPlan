<template>
  <div class="flex flex-col bg-gray-200 p-4 h-screen overflow-auto"> <h2>Team Information</h2>
    <div class="mt-4">
      <label for="team-name" class="text-sm font-medium block mb-1">Team Name:</label>
      <input id="team-name" type="text" :value="team.name" readonly class="block w-full px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-indigo-500 focus:ring-1">
    </div>
    <div class="mt-4">
      <label for="attributed-teacher" class="text-sm font-medium block mb-1">Attributed Teacher:</label>
      <select id="attributed-teacher" v-model="team.attributedTeacher" class="block w-full px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-indigo-500 focus:ring-1">
        <option v-for="teacher in teachers" :key="teacher.id" :value="teacher.id">{{ teacher.name }}</option>
      </select>
    </div>
    <div class="mt-4">
      <label for="members" class="text-sm font-medium block mb-1">Members:</label>
      <ul class="list-disc pl-4"> <li v-for="member in team.members" :key="member.id">{{ member.name }}</li>
      </ul>
    </div>
    <div class="mt-4">
      <label for="grades" class="text-sm font-medium block mb-1">Grades:</label>
      <ul class="list-disc pl-4">
        <li v-for="grade in team.grades" :key="grade.id">{{ grade.value }}</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const team = ref({
  name: "Team A",
  attributedTeacher: "",
  members: [
    { id: 1, name: "John" },
    { id: 2, name: "Jane" },
    { id: 3, name: "Mike" },
  ],
  grades: [
    { id: 1, value: "A" },
    { id: 2, value: "B" },
    { id: 3, value: "C" },
  ],
});

const teachers = [
  { id: 1, name: "Teacher 1" },
  { id: 2, name: "Teacher 2" },
  { id: 3, name: "Teacher 3" },
];
</script>

<style scoped>
/* Override default styles for a cleaner look (optional) */
.list-disc {
  list-style-type: disc;
  margin-left: 0;
  padding-left: 1rem;
}
</style>
