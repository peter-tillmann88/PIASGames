const express = require('express');
const { createClient } = require('@supabase/supabase-js');
const cors = require('cors'); // Import cors
require('dotenv').config(); // Load environment variables

const app = express();
const port = 3000;

// Initialize Supabase client
const supabase = createClient(
  process.env.SUPABASE_URL,
  process.env.SUPABASE_API_KEY
);

// Configure CORS
app.use(cors({
  origin: process.env.FRONTEND_URL,
  methods: ['GET', 'POST', 'PUT', 'DELETE'],
  allowedHeaders: ['Content-Type', 'Authorization'],
}));

app.get('/generate-signed-url', async (req, res) => {
  const { bucketName, fileName } = req.query;

  if (!bucketName || !fileName) {
    return res.status(400).json({ error: 'Bucket name and file name are required' });
  }

  try {
    const { data, error } = await supabase
      .storage
      .from(bucketName)
      .createSignedUrl(fileName, 60 * 60); // 1 hour

    if (error) {
      throw error;
    }

    res.json({ signedUrl: data.signedUrl });
  } catch (error) {
    console.error('Error generating signed URL:', error);
    res.status(500).json({ error: 'Failed to generate signed URL', details: error.message });
  }
});

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
