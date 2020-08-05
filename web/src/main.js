import App from './App.svelte';

const app = new App({
	target: document.body,
	props: {
		name: 'world'
	}
});
window.apiKey = process.env.apiKey;
window.app = app;

export default app;